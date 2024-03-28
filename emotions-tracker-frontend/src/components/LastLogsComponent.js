import React from "react";
import EntryLoggingApiClient from "../services/EntryLoggingApiClient";
import "./LastLogsComponent.css"
import DateFormatView from "../helper_components/DateFormatView";
import StateView from "../helper_components/StateView";
import EmotionsView from "../helper_components/EmotionsView";
import {getUserId} from "../services/AuthApiClient";

export class LastLogsComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            lastLogsData: [],
            serverError: false
        }
    }

    componentDidMount() {
        this.refreshLastLogs();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.statsRefresh === true) {
            this.refreshLastLogs();
            this.props.setStatsRefresh(false);
        }
    }

    getLastLogs(): Promise {
        return EntryLoggingApiClient.lastLogs(getUserId()).then(
            res => {
                if (res.ok) {
                    return res.json();
                } else {
                    return Promise.reject("Error on fetching last logs");
                }
            }
        );
    }

    updateLastLogs(lastLogs) {
        this.setState({
            lastLogsData: lastLogs,
            serverError: false
        });
    }

    refreshLastLogs() {
        this.getLastLogs().then(
            lastLogs => {
                this.updateLastLogs(lastLogs);
            }
        ).catch(reason => {
            this.setState({serverError: true});
            console.log('Entry logging server error', reason);
        });
    }


    render() {
        if (this.state.serverError) {
            return (
                <div>We're sorry, but we can't display last logged states at the moment</div>
            );
        }
        return (
            <div>
                {this.state.lastLogsData.map(logs =>
                    <div className="state-log-div" key={logs.id}>
                        <DateFormatView value={logs.dateTime}/>
                        <StateView value={logs.state}/>
                        <EmotionsView emotions={logs.emotions}/>
                    </div>)}
            </div>
        );
    }

}