   @Override
    public String submitFormAToFinacle(FormADTO formA) {
        try {
            String response = "";
            String message;

            HashMap<String, String> map = new HashMap<>();
            HashMap<String, Date> map2 = new HashMap<>();


            FormA dbFormA = formARepo.findByTempFormNumber(formA.getTempFormNumber());

            if (dbFormA !=null) {

                BankUser bankUser = bankUserService.getUserByName(formA.getApproverName());

                FormADTO formADTO = formAService.convertEntityTODTO(dbFormA);
                logger.info("FormA beneficiary name 2 {} ", formADTO.getBeneficiaryName());
                List<CommentDTO> commentDTO = formAService.getComments(dbFormA);
                commentDTO.forEach(commentDtd -> {
                    commentDtd.getUsername();
                    commentDtd.getComment();
                    commentDtd.getMadeOn();
                    map.put("userName", commentDtd.getUsername());
                    map.put("comment", commentDtd.getComment());
                    map2.put("madeOn", commentDtd.getMadeOn());
                });
                formADTO.setComment(map.get("comment"));
                formADTO.setAuthFirstname(bankUser.getFirstName());
                formADTO.setAuthLastname(bankUser.getLastName());
                formADTO.setMadeOn(map2.get("madeOn"));
                formADTO.setSortCode(bankUser.getBranch().getSortCode());

               ServiceAuditUpgrade serviceAudit = saveToAudit(formADTO);

                if( serviceAudit.getNoOfAttempt() >=retry) {
                    dbFormA.setSubmitFlag("F");
                    formARepo.save(dbFormA);
                  sendITSMail.FormAFailureMail(dbFormA);


                }


                    String uri = URI + "/api/forma/submit";

                        logger.info("abt calling tradeservice =======");


                        formResponse = template.postForObject(uri, formADTO, FormResponse.class);
                        logger.info("The Main of FormA FormResponse  {}", formResponse);
                        formResponse.setTempFormNum(dbFormA.getTempFormNumber());
                        response = formResponse.getResponseType();
                        message= formResponse.getResponseMessage();



                        if (response.equalsIgnoreCase("Y")) {

                            serviceAudit.setResponseFlag("Y");
                            serviceAudit.setResponseMessage(message);
                            dbFormA.setSubmitFlag("S");
                            formARepo.save(dbFormA);

                            //pushing to a FormA FI queue
                            amqpTemplate.convertAndSend(updateTmsFormA, mapper.writeValueAsString(formResponse));


                        } else if (response.equalsIgnoreCase("S")) {
                            dbFormA.setStatus("PV");
                            dbFormA.setSubmitFlag("S");
                            formARepo.save(dbFormA);
                            serviceAudit.setResponseMessage(formResponse.getResponseMessage());
//                            serviceAuditRepo.save(serviceAudit);
                        } else {
                            serviceAudit.setResponseFlag("N");
                            serviceAudit.setResponseMessage(message);
                            logger.error("Error response for Form A {} ", message);
//                            serviceAuditRepo.save(serviceAudit);
//
                        }
                         serviceAuditRepo.save(serviceAudit);


            }
            return response;
        } catch (Exception e) {
            logger.info("Form A error in catch {} ", e.getMessage());
            logger.info("Error pushing Form A to finacle {} ", e);
            ServiceAuditUpgrade serviceAudit =saveToAudit(formA);
            serviceAudit.setResponseMessage(e.getMessage());
            serviceAuditRepo.save(serviceAudit);



        }
        return null;
    }