'''
Created on Feb 7, 2019

@author: LONGBRIDGE
'''
from flask import Flask, render_template,request
from flask_sqlalchemy import SQLAlchemy
from send_email import send_email
from sqlalchemy import  func

app=Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:postgres321@localhost/height_collector'
db = SQLAlchemy(app)

class Data (db.Model):
    __tablename__ = "data"
    id= db.Column(db.Integer, primary_key= True)
    email_=db.Column(db.String(120), unique= True)
    height_=db.Column(db.Integer)
    
    def __init__(self,email_,height_):
        self.email_ = email_
        self.height_= height_
    
#db.create_all ()

@app.route("/")
def Index():
    return render_template("index.html")

@app.route("/success", methods=['POST'])
def Success():
    if request.method=='POST' :
        email =request.form["email_name"]
        height =request.form["height_name"]
        print(request.form)
        if db.session.query(Data).filter(Data.email_== email).count() == 0 :
            data=Data(email,height)
            db.session.add(data)
            db.session.commit()
            average_height_= db.session.query(func.avg(Data.height_)).scalar()
            average_height_= round(average_height_,1)
            count= db.session.query(Data.height_).count()
            send_email(email, height,average_height_,count)
            print(average_height_)
            return render_template("success.html")
    return render_template("index.html",
                            text="seems like we've got something from that email address already!")
    


if __name__== "__main__" :
    app.run(port=5001)
    app.debug=True
    
    
     