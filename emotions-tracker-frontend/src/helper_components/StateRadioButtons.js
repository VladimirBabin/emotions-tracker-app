import './StateRadioButtons.css'
import * as React from "react";
import RadioButton from "./RadioButton";

const states = [
    "Awful",
    "Bad",
    "Ok",
    "Good",
    "Excellent",
];

const StateRadioButtons = ({handleRadiobutton}) => {
    return (
        <div className="radiobutton-div">
            {states.map(s => <RadioButton key={s} text={s} onChange={handleRadiobutton}/>)}
        </div>
    )
}

export default StateRadioButtons;