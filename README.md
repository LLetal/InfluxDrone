# InfluxDrone
Simple android application for collection of telemetry information of DJI drones. The application itself is written in java and is based on DJI mobile SDK version 4.16.2. Data from the drone is collected in real time and is stored on the InfluxDB server. Application at this stage allows InfluxDB as the only applicable database, but I may add more options in the future. At this moment only monitoring of xyz velocities and battery metrics is avaliable, but I may add more metrics in the future. Lastly I would like to emphasize, that this app is my first project in java and in the field of mobile apps, so don't hesitate to report bugs, and add suggestions for fixes.
# Prerequisities
To launch and use this application you have to own any product displayed on this site https://developer.dji.com/products/#!/mobile and android phone with at least android 8.
# How to use
First of all, you have to clone this repository and build it in the IDEA of your choice (I have used android studio). Dont forget to paste your google map API key(https://developers.google.com/maps/documentation/javascript/get-api-key), without it the newly added map widget wouldn't launch.Once the application is built, turn on your drone and plug your phone into the RC controller. Once the app is launched the DJI SDK Authorization automatically starts. To successfully register the app you have to be connected to the internet and it sadly is impossible to continue further without a completed registration (app crashes). 
<p align="center">
<img src="https://github.com/LLetal/InfluxDrone/blob/main/images/Mainactivity.jpg" alt="Registration completed" width="270"/>
</p>
After successful authorization, you can just fill in the organization, bucket, API token, and finally URL of desired InfluxDB server and click submit button. After clicking the button the app will send test data to your InfluxDB server to confirm, that the submitted information is valid. If you have filled in valid information you should see this:
<p align="center">
<img src="https://github.com/LLetal/InfluxDrone/blob/main/images/secondactivity.jpg" alt="drawing" width="270"/>
</p>
In the opposite situation your application will crash after clicking submit button, which means that you have probably submitted a wrong information or desired server is not reachable. Once you successfully reach the "click for data" button you can just manually take off with your drone (move both joysticks to opposite corners) and click the button once you want to start data collection.


# Showcase
To demonstrate functionality of my app I have recorded a short video of drone flight and pasted the measured data during that short flight. I have also made two dashboards, one of them in grafana and second one in influxdb, both of the current dashboards are inside of showcase forlder.

![Dashboard grafana](https://github.com/LLetal/InfluxDrone/blob/main/images/grafana_dashboard.jpg)

![Dashboard Influxdb](https://github.com/LLetal/InfluxDrone/blob/visualization/images/Influx_dashboard.png)
[https://youtube.com/shorts/ePIWshUOIoY?feature=share](https://youtube.com/shorts/ePIWshUOIoY)

# Datasets 
To demonstrate function of my application I have pasted two datasets to showcase folder. First dataset is matching video pasted above and dashboard doesnt  display geometrics from this dataset, since when I was recording it, the geometrics collection wasn't working yet. On the other hand, second dataset does display all at the moment aviable collected metrics, but sadly doesnt have video of drone attached.

# Data collection
As mentioned above, data from drone is being sent to influxDB server, while the data extraction is turned on. To monitor the data I have implemented a grafana dashboard, which is aviliable inside of grafana folder. The data is saved automatically under meassurement with name of your drone device.
