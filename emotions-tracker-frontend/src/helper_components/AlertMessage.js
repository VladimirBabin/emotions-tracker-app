import './AlertMessage.css'
import React from "react";
import AlertTitle from "./AlertTitle";
import AlertText from "./AlertText";

const AlertMessage = ({ message, onClick }) => {
    return (
        <div key={message} className="alert-div">
            <span className="alert-close-btn" onClick={onClick}>&times;</span>
            <strong>{AlertTitle(message)}</strong> {AlertText(message)}
        </div>
    )
};

export default AlertMessage;