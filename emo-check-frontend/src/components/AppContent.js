import React from 'react';
import Popup from "../custom-classes/Popup";
import {useState} from 'react';
import './AppContent.css'
import WeeklyStatsComponent from "./WeeklyStatsComponent";



function AppContent() {
    const [buttonPopup, setButtonPopup] = useState(false);

    return (
        <div>
            <div className="plus-button-title">
                <h6>To log your state tap the button:</h6>
            </div>
            <div className="plus-button-div">
                <button className="plus-button" onClick={() => setButtonPopup(true)}>+</button>
            </div>
            <div className="container-fluid">
                <div className="weekly-stats-div">
                    <WeeklyStatsComponent/>
                </div>
            </div>
            <div>
                <Popup trigger={buttonPopup} setTrigger={setButtonPopup}/>
            </div>
        </div>
    );
}

export default AppContent;