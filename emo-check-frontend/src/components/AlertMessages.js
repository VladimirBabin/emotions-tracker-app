import React from "react";
import ApiClient from "../services/ApiClient";
import './AlertMessages.css'
import AlertMessage from "../helper_components/AlertMessage";

export class AlertMessages extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            alerts: [],
            displayAlerts: false,
            shown: []
        }
    }

    componentDidMount() {
        this.refreshAlerts();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.statsRefresh === true) {
            this.refreshAlerts();
            this.props.setStatsRefresh(false);
        }
    }

    getAlerts(): Promise {
        // hardcoded for id = 52 until authorization isn't linked to emo-check and users have different ids
        return ApiClient.checkForRecentAlerts(52).then(
            res => {
                if (res.ok) {
                    return res.json();
                } else {
                    return Promise.reject("Error on fetching alerts");
                }
            }
        );
    }

    updateAlerts(alerts) {
        if (alerts !== null) {
            alerts.map(alert => {
                if (!this.state.shown.includes(alert) && !this.state.alerts.includes(alert)) {
                    this.state.alerts.push(alert);
                }
            })
        }
    }

    refreshAlerts() {
        this.getAlerts().then(
            alerts => {
                if (alerts !== null) {
                    this.updateAlerts(alerts)
                }
            }
        ).catch(reason => {
            this.setState({displayAlerts: false});
            console.log('Alerts server error', reason);
        });
        if (this.state.alerts !== null && this.state.alerts.length !== 0) {
            this.setState({
                displayAlerts: true
            })
        }
    }

    closeAlert(alertMessage: string) {
        this.state.shown.push(alertMessage);
        let filtered = this.state.alerts.filter(alert => alert !== alertMessage);
        this.setState({
            alerts: filtered
        })
    }


    render() {
        return (
            <div>
                {this.state.displayAlerts && this.state.alerts.map(alertMessage =>
                    !this.state.shown.includes(alertMessage) &&
                    <AlertMessage message={alertMessage} onClick={() => this.closeAlert(alertMessage)}/>
                )
                }</div>
        );
    }
}