# Image-Recognition-Software
InceptionV3 as a FLask based Web API with JFrame GUI on Desktop

Before Running:

H5 file required to load weights into architecture of inception : https://github.com/fchollet/deep-learning-models/releases
Download -> inception_v3_weights_tf_dim_ordering_tf_kernels.h5 ( Save it in templates/inception_h5 )

Required Jar Files ( To support FileUpload and Http Request )
1. https://commons.apache.org/proper/commons-fileupload/download_fileupload.cgi
2. https://hc.apache.org/downloads.cgi


Flask Code folder contains: 
1. Web API  { __init__.py }
2. Inception { templates -> python -> cnn -> Inceptionv3.py }

Java Files folder contains:
1. java user interface { IReS.java }

![alt text](https://github.com/VamshikShetty/Project/blob/master/Image%20Recognition%20Software/example.png)
