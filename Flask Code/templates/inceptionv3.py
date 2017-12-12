from keras.applications.inception_v3 import InceptionV3
from keras.preprocessing import image
from keras.applications.inception_v3 import preprocess_input, decode_predictions

import numpy as np
import os
import cv2

current_folder = os.path.dirname(os.path.abspath(__file__))

#
# for better understanding of funciton vist : https://keras.io/
#
# For inception_v3_weights_tf_dim_ordering_tf_kernels.h5 go to https://github.com/fchollet/deep-learning-models/releases

def load_model():
    model = InceptionV3(weights=None)
    model.load_weights(current_folder+'\\inception_h5\\inception_v3_weights_tf_dim_ordering_tf_kernels.h5')
    return model


def load_image(img,model):
    # Resizing the image 299x299 
    img = cv2.resize(img,None,fx=(299/img.shape[1]),fy=(299/img.shape[0]),interpolation=cv2.INTER_CUBIC)              
    img = np.expand_dims(img, axis=0)

    # normalizing the value of each pixel between 0 and 1 
    img=img*1./255

    # predicting the result for given image
    preds = model.predict(img)

    # Get the top three Prediction
    value = decode_predictions(preds, top=3)[0]
    return value


