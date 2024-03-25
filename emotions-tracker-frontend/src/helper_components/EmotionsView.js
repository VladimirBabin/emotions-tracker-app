import "./EmotionsView.css"
import EmotionsColour from "./EmotionsColour";
import React from "react";

const EmotionsView = ({emotions}) => {

    return (
        <div className="emotions-view-div">{emotions.map(e =>
            <p key={e} className="emotion-view-item" style={{backgroundColor: EmotionsColour(e)}}>
                {e.charAt(0) + e.toLowerCase().slice(1)}
            </p>)}
        </div>

    );
};

export default EmotionsView;