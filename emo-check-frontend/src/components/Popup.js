import * as React from 'react';
import './Popup.css'
import LogStateComponent from "./LogStateComponent";

function Popup (props) {
    return (props.trigger) ? (
        <div className="popup">
            <div className="popup-inner">
                <LogStateComponent trigger={props.trigger} setTrigger={props.setTrigger}/>
                <button className="close-btn" onClick={() =>  {
                    props.setTrigger(false);
                    props.setStatsRefresh(true);
                }}>close</button>
            </div>
        </div>
    ) : "";
}

export default Popup
