import React, {useState} from 'react';
import Popup from "../helper_components/Popup";
import './AppContent.css'
import WeeklyStatsComponent from "./WeeklyStatsComponent";
import {LastLogsComponent} from "./LastLogsComponent";


function AppContent() {
    const [buttonPopup, setButtonPopup] = useState(false);
    const [statsRefresh, setStatsRefresh] = useState(false);

    return (
        <div>
            <div className="plus-button-title">
                <h5>To log your state tap the button:</h5>
            </div>
            <div className="plus-button-div">
                <button className="plus-button" onClick={() => setButtonPopup(true)}>+</button>
            </div>
            <div className="container-fluid">
                <div className="weekly-stats-div">
                    <WeeklyStatsComponent statsRefresh={statsRefresh} setStatsRefresh={setStatsRefresh}/>
                </div>
                <div className="last-logs-div">
                    <LastLogsComponent statsRefresh={statsRefresh} setStatsRefresh={setStatsRefresh}/>
                </div>
            </div>
            <div>
                <Popup trigger={buttonPopup} setTrigger={setButtonPopup} setStatsRefresh={setStatsRefresh}/>
            </div>
        </div>
    );
}

export default AppContent;