import "./EmotionView.css"
import EmotionsColour from "./EmotionsColour";
import React from "react";

const EmotionView = ({emotion}) => {

    return (
        <p className="emotion-view-item" style={{backgroundColor: EmotionsColour(emotion)}}>
            {emotion.charAt(0) + emotion.toLowerCase().slice(1)}
        </p>)
};

export default EmotionView;