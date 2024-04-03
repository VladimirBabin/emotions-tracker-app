import * as React from 'react';
import './Popup.css'
import LogStateComponent from "./LogStateComponent";
import {useState} from "react";
import {DemoContainer} from '@mui/x-date-pickers/internals/demo';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import { MobileDateTimePicker } from '@mui/x-date-pickers/MobileDateTimePicker';

function Popup(props) {

    const [date, setDate] = useState(null);

    switch (props.trigger) {
        case "daySelectView":
            return (
                <div className="popup">
                    <div className="popup-inner">
                        <div className="row row-no-gutters">
                            <div className="col-md-5">
                                <button className="btn btn-success btn-block"
                                        onClick={() => {
                                            setDate(null);
                                            props.setTrigger("logStateView");
                                        }}>
                                    Today
                                </button>
                            </div>
                            <div className="col-md-5">
                                <button className="btn btn-primary btn-block"
                                        onClick={() => {
                                            props.setTrigger("calendarView");
                                        }}>
                                    Other
                                </button>
                            </div>
                        </div>
                        <button className="close-btn" onClick={() => {
                            props.setAlertsRefresh(true);
                            props.setTrigger("none");
                            props.setStatsRefresh(true);
                        }}>close
                        </button>
                    </div>
                </div>
            )
        case "calendarView":
            return (
                <div className="popup">
                    <div className="popup-inner">
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DemoContainer components={['DateTimePicker', 'DateTimePicker']}>
                                <MobileDateTimePicker
                                    label="Choose date and time"
                                    onChange={(newDate) => setDate(newDate)}
                                />
                            </DemoContainer>
                        </LocalizationProvider>
                        <button className="btn btn-primary" onClick={() => props.setTrigger("logStateView")}>Continue
                        </button>
                        <button className="close-btn" onClick={() => {
                            props.setAlertsRefresh(true);
                            props.setTrigger("none");
                            props.setStatsRefresh(true);
                        }}>close
                        </button>
                    </div>
                </div>
            )
        case "logStateView":
            return (
                <div className="popup">
                    <div className="popup-inner">
                        <LogStateComponent date={date} trigger={props.trigger} setTrigger={props.setTrigger}/>
                        <button className="close-btn" onClick={() => {
                            props.setAlertsRefresh(true);
                            props.setTrigger("none");
                            props.setStatsRefresh(true);
                        }}>close
                        </button>
                    </div>
                </div>
            )
        case "none":
            return ""
        default:
            return ""
    }
}

export default Popup
