import * as React from 'react';
import './Popup.css'
import StateComponent from "../components/StateComponent";

function Popup (props) {
    return (props.trigger) ? (
        <div className="popup">
            <div className="popup-inner">
                <StateComponent trigger={props.trigger} setTrigger={props.setTrigger}/>
                <button className="close-btn" onClick={() => props.setTrigger(false)}>close</button>
            </div>
        </div>
    ) : "";
}

export default Popup
