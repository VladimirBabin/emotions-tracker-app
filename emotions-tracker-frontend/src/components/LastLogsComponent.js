import React from "react";
import EntriesApiClient from "../services/EntriesApiClient";
import "./LastLogsComponent.css"
import LogsDateTimeView from "../helper_components/LogsDateTimeView";
import StateView from "../helper_components/StateView";
import EmotionsView from "../helper_components/EmotionsView";
import {getUserId} from "../services/AuthApiClient";
import CommentView from "../helper_components/CommentView";

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

    sendDeleteRequest(entryId: number): void {
        return EntriesApiClient.deleteEntry(entryId).then(
            res => {
                if (!res.ok) {
                    console.log("Error on deleting the entry " + entryId);
                }
            }
        )
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

    deleteEntry(id) {
        this.sendDeleteRequest(id);
        this.refreshLastEntries();
        this.props.setStatsRefresh(true);
    }


    render() {
        if (this.state.serverError) {
            return (
                <div>We're sorry, but we can't display last logged entries at the moment</div>
            );
        }
        return (
            <div>
                {this.state.lastEntries.map(entry =>
                    <div className="last-entry-div" key={entry.id}>
                        <div className="state-log-div" key={entry.id}>
                            <LogsDateTimeView value={entry.dateTime}/>
                            <StateView value={entry.state}/>
                            <EmotionsView emotions={entry.emotions}/>
                        </div>
                        {(entry.comment === null || entry.comment === "")
                            ? ""
                            : <CommentView comment={entry.comment}/>}
                        <button onClick={() => this.deleteEntry(entry.id)} className="x-button">
                            <svg className="x-button-icon" fill="none" viewBox="0 0 24 24" stroke="currentColor" display="visible">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>)}
            </div>
        );
    }

}