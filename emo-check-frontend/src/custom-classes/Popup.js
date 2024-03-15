import * as React from 'react';
import './Popup.css'
import StateComponent from "../components/StateComponent";

function Popup (props) {
    return (props.trigger) ? (
        <div className="popup">
            <div className="popup-inner">
                <StateComponent/>
                <button className="close-btn" onClick={() => props.setTrigger(false)}>close</button>
                { props.children }
            </div>
        </div>
    ) : "";
}

export default Popup
