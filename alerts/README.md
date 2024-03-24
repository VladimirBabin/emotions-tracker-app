This is a microservice responsible for detecting alerts based on triggering states and emotions 
user logged, there frequency and repetitiveness.

Alerts are de-coupled from how they might be interpreted and contain only short message 
as a description. Front-end is responsible for generating the full messages based on alert.

Alerts available can be found in description field of EmotionAlertType:
- stressed-slightly
- stressed-highly
- stressed-regularly
- depressed-slightly
- drained-slightly
- scared-slightly
and StateAlertType: 
- resources-low
- resources-lowest