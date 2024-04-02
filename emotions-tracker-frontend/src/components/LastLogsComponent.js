import React from "react";
import EntriesApiClient from "../services/EntriesApiClient";
import "./LastLogsComponent.css"
import LogsDateTimeView from "../helper_components/LogsDateTimeView";
import StateView from "../helper_components/StateView";
import EmotionsView from "../helper_components/EmotionsView";
import {getUserId} from "../services/AuthApiClient";

export class LastLogsComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            lastEntries: [],
            serverError: false
        }
    }

    componentDidMount() {
        this.refreshLastEntries();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.statsRefresh === true) {
            this.refreshLastEntries();
            this.props.setStatsRefresh(false);
        }
    }

    getLastLogs(): Promise {
        return EntriesApiClient.getLastEntries(getUserId()).then(
            res => {
                if (res.ok) {
                    return res.json();
                } else {
                    return Promise.reject("Error on fetching last entries");
                }
            }
        );
    }

    updateLastEntries(lastEntries) {
        this.setState({
            lastEntries: lastEntries,
            serverError: false
        });
    }

    refreshLastEntries() {
        this.getLastLogs().then(
            lastEntries => {
                this.updateLastEntries(lastEntries);
            }
        ).catch(reason => {
            this.setState({serverError: true});
            console.log('Entries server error', reason);
        });
    }


    render() {
        if (this.state.serverError) {
            return (
                <div>We're sorry, but we can't display last logged entries at the moment</div>
            );
        }
        return (
            <div>
                {this.state.lastEntries.map(logs =>
                    <div className="state-log-div" key={logs.id}>
                        <LogsDateTimeView value={logs.dateTime}/>
                        <StateView value={logs.state}/>
                        <EmotionsView emotions={logs.emotions}/>
                    </div>)}
            </div>
        );
    }

}