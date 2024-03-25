import * as React from "react";

export default function AlertText (message: string) {
    switch (message) {
        case "stressed-slightly":
            return "Low-to-moderate stress may be beneficial for your brain function, " +
                "especially when it comes to working memory. However, when stress levels " +
                "rise above moderate levels, they can become toxic. To monitor your stress level, " +
                "log it regularly in the app";
        case "stressed-highly":
            return "If you’re struggling with stress and don’t know how to cope, " +
                "you may want to seek help from a specialist. Your primary care doctor " +
                "can be a good starting point. They can help you figure out if the signs " +
                "and symptoms you’re experiencing are from a medical issue or an anxiety disorder."
        case "stressed-regularly":
            return "Although stress is unavoidable, being chronically stressed takes a toll " +
                "on your physical and mental health. Fortunately, several evidence-based strategies " +
                "can help you reduce stress and improve your overall psychological well-being. " +
                "Exercise, mindfulness, spending time with a pet, minimizing screen time, and " +
                "getting outside more often are all effective methods."
        case "depressed-slightly":
            return "Feeling down or depressed from time to time is normal. But if these feelings last " +
                "2 weeks or more, or start to affect everyday life, this can be a sign of depression. " +
                "Take up some form of exercise. There's evidence that exercise can help lift your mood. " +
                "If you haven't exercised for a while, start gently by walking for 20 minutes every day."
        case "drained-slightly":
            return "Research shows that people suffering from emotional exhaustion experience higher levels" +
                " of work-life conflict. They may find that they have less patience to engage with family and" +
                " friends at the end of the day and become frustrated with them more easily — a problem that " +
                "is exacerbated by the current Covid-19 crisis. That’s why it’s important to remember to pay " +
                "attention to your emotional energy and note when reserves run low. Learn what factors tend to " +
                "drain them and experiment with ways to reduce the strain. Fix what you can, and learn how to " +
                "more effectively handle what you can’t. Think about what values and qualities drive you and " +
                "practice being centered and present for short periods of time to create more experiences of " +
                "joy and connection."
        case "scared-slightly":
            return "Learning relaxation techniques can help you with the mental and physical feelings of fear. " +
                "It can help just to drop your shoulders and breathe deeply. Or imagine yourself in a relaxing " +
                "place. You could also try complementary therapies or exercise such as massage, t’ai chi, " +
                "yoga, mindfulness techniques, or meditation."
        case "resources-low":
            return "It's important to take care of your inner self and fill your energy level from time " +
                "to time by doing something you like. Don't let your resources drop low. You can prevent " +
                "a lot of unpleasant conditions by regularly doing something that makes you happy. " +
                "If it's not clear what kind of activity that can be, consider trying something new. " +
                "Remember to be optimistic and lower your expectations, chances are high you'll " +
                "find more pleasure and take up a new hobby or make new friends."
        case "resources-lowest":
            return "It’s absolutely natural not to feel at your best from time to time, " +
                "especially during times of stress or when busier than usual. " +
                "Remember, you’re the best judge of your own needs. Sometimes, doing nothing is exactly what you need" +
                " — and that’s okay. Just take care to pay attention to other signs that may alert you to something" +
                " else going on. However, if you feel loss of interest in things you usually enjoy, emptiness and " +
                "persistent low mood - it might be best to talk to a therapist."
    }
}