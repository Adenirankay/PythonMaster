'''
Created on Mar 5, 2019

@author: LONGBRIDGE
'''
import cv2, time

first_frame = None
video = cv2.VideoCapture(0)

while True:
   
    check, frame = video.read()
    
    gray= cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    gray= cv2.GaussianBlur(gray,(21,21),0)
    
    if first_frame is None :
        first_frame = gray
        continue
    
    delta_frame = cv2.absdiff(first_frame,gray)
    thresh_frame = cv2.threshold(delta_frame,30,255,cv2.THRESH_BINARY)[1]
    thresh_frame = cv2.dilate(thresh_frame,None,iterations=2)
    

    cv2.imshow("capturing", gray)
    cv2.imshow("delta frame",delta_frame)
    cv2.imshow("tresh", thresh_frame)
    key = cv2.waitKey(1)
    
    if key==ord('q') :
        break
    
#print(a)
video.release()
cv2.destroyAllWindows()xb


