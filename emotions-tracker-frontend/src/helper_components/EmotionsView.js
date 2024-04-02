import "./EmotionsView.css"
import React from "react";
import EmotionView from "./EmotionView";

const EmotionsView = ({emotions}) => {

    return (
        <div className="emotions-view-div">{emotions.map(e =>
            <EmotionView keyValue={e} emotion={e} />)}
        </div>
    );
};

export default EmotionsView;