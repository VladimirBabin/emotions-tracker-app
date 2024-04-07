This is a microservice responsible for detecting alerts based on triggering 
states and emotions the user logged, their frequency, and repetitiveness.

Alerts are de-coupled from how they might be interpreted and contain only 
a short message as a description. Front-end is responsible for generating 
the full messages based on alerts.

Alerts available can be found in the description field of 
- EmotionAlertType:
  - stressed-slightly (shown when a user logs stress once a week)
  - stressed-highly (shown when a user logs stress five times a week)
  - stressed-regularly (shown when a user logs stress at least once every four weeks)
  - depressed-slightly (shown when a user logs sadness five times a week)
  - drained-slightly (shown when a user logs drained five times a week)
  - scared-slightly (shown when a user logs scared five times a week)

- StateAlertType: 
  - resources-low (when the low state is logged twice a week)
  - resources-lowest (when the low state is logged seven times a week)
      
#### Here are the text messages the user gets:

- **stressed-slightly**

Low-to-moderate stress may be beneficial for your brain function, especially when it comes to working memory. However, when stress levels rise above moderate levels, they can become toxic. To monitor your stress level, log it regularly in the app.
- **stressed-highly**

If you’re struggling with stress and don’t know how to cope, you may want to seek help from a specialist. Your primary care doctor can be a good starting point. They can help you figure out if the signs and symptoms you’re experiencing are from a medical issue or an anxiety disorder.
- **stressed-regularly**

Although stress is unavoidable, being chronically stressed takes a toll on your physical and mental health. Fortunately, several evidence-based strategies can help you reduce stress and improve your overall psychological well-being. Exercise, mindfulness, spending time with a pet, minimizing screen time, and getting outside more often are all effective methods.
- **depressed-slightly**

Feeling down or depressed from time to time is normal. But if these feelings last 2 weeks or more, or start to affect everyday life, this can be a sign of depression. Take up some form of exercise. There's evidence that exercise can help lift your mood. If you haven't exercised for a while, start gently by walking for 20 minutes every day.
- **drained-slightly**

Research shows that people suffering from emotional exhaustion experience higher levels of work-life conflict. They may find that they have less patience to engage with family and friends at the end of the day and become frustrated with them more easily — a problem that is exacerbated by the current COVID-19 crisis. That’s why it’s important to remember to pay attention to your emotional energy and note when reserves run low. Learn what factors tend to drain them and experiment with ways to reduce the strain. Fix what you can, and learn how to more effectively handle what you can’t. Think about what values and qualities drive you and practice being centered and present for short periods of time to create more experiences of joy and connection.
- **scared-slightly**

Learning relaxation techniques can help you with the mental and physical feelings of fear. It can help just to drop your shoulders and breathe deeply. Or imagine yourself in a relaxing place. You could also try complementary therapies or exercises such as massage, t’ai chi, yoga, mindfulness techniques, or meditation.
- **resources-low**

It's important to take care of your inner self and fill your energy level from time to time by doing something you like. Don't let your resources drop low. You can prevent a lot of unpleasant conditions by regularly doing something that makes you happy. If it's not clear what kind of activity that can be, consider trying something new. Remember to be optimistic and lower your expectations, chances are high you'll find more pleasure and take up a new hobby or make new friends.

- **resources-lowest**

It’s absolutely natural not to feel at your best from time to time, especially during times of stress or when busier than usual. Remember, you’re the best judge of your own needs. Sometimes, doing nothing is exactly what you need — and that’s okay. Just take care to pay attention to other signs that may alert you to something else going on. However, if you feel loss of interest in things you usually enjoy, emptiness, and persistent low mood - it might be best to talk to a therapist.
