import sys
from PIL import Image
from flask import Flask, request
import numpy as np
import os
import cv2
import keras
import inceptionv3 as cnn

current_folder = os.path.dirname(os.path.abspath(__file__))

# define path to where your classifier is located
sys.path.insert(0, current_folder+'\\templates\\')




app = Flask(__name__)


model = cnn.load_model()

# once the model is loaded we need to make it thread ready
# its a keras function but has no offical documentation on it
model._make_predict_function()

@app.route('/inceptionv3/',methods=['GET','POST'])
def sending_data():
        # Check if the post request was made
        if request.method=='POST':
                # check if there exist a file with the 'pic' as key for its name value pair
                if 'pic' in request.files:
                        try:
                                # get the the file
                                src = request.files['pic']

                                # convert it into image format
                                pilImg = Image.open(src.stream)

                                #here we get the image in matix form
                                npImage = np.array(pilImg)

                                # function called from cnn module in template -> python -> cnn Folder  
                                value = cnn.load_image(npImage,model)

                                # it returns a dictonary
                                str_v=""
                                for i in value:
                                        str_v = str_v + str((i[1],i[2]))+"\n"
                                
                                return  str_v
                        except Exception as e:
                                return (str(e))
                        return "file not uploaded"
        else:
                return "Please send a post request"

# Run if it is the main function
if __name__=="__main__":
	app.run()


