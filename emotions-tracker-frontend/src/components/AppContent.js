import React, {useState} from 'react';
import Popup from "./Popup";
import './AppContent.css'
import WeeklyStatsComponent from "./WeeklyStatsComponent";
import {LastLogsComponent} from "./LastLogsComponent";
import {AlertMessages} from "./AlertMessages"


function AppContent() {
    const [buttonPopup, setButtonPopup] = useState(false);
    const [statsRefresh, setStatsRefresh] = useState(false);
    const [alertsRefresh, setAlertsRefresh] = useState(false);

    return (
        <div>
            <AlertMessages alertsRefresh={alertsRefresh} setAlertsRefresh={setAlertsRefresh}/>
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

            </div>
            <div>
                <Popup trigger={buttonPopup} setTrigger={setButtonPopup} setStatsRefresh={setStatsRefresh}
                       setAlertsRefresh={setAlertsRefresh}/>
            </div>
        </div>
    );
}

export default AppContent;