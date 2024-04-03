import './EmotionCheckboxes.css'
import Checkbox from "./Checkbox";
import * as React from "react";

const firstEmotionsGroup = [
    "Happy",
    "Indifferent",
    "Sad",
    "Excited",
    "Peaceful",
    "Anxious"
];
const secondEmotionsGroup = [
    "Satisfied",
    "Content",
    "Drained",
    "Passionate",
    "Stressed",
    "Angry"
];
const thirdEmotionsGroup = [
    "Tired",
    "Hopeful",
    "Irritated",
    "Surprised",
    "Scared",
    "Jealous",
];

const EmotionCheckboxes = ({handleCheckbox}) => {
    return (
        <div>
            <div className="checkbox-div">
                {firstEmotionsGroup.map(e => <Checkbox key={e} text={e} onChange={handleCheckbox}/>)}
            </div>
            <div className="checkbox-div">
                {secondEmotionsGroup.map(e => <Checkbox key={e} text={e} onChange={handleCheckbox}/>)}
            </div>
            <div className="checkbox-div">
                {thirdEmotionsGroup.map(e => <Checkbox key={e} text={e} onChange={handleCheckbox}/>)}
            </div>
        </div>
    )
}

export default EmotionCheckboxes;