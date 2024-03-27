import React from "react";
import './AlertMessages.css'
import AlertMessage from "../helper_components/AlertMessage";
import {getUserId} from "../services/AuthApiClient";
import AlertsApiClient from "../services/AlertsApiClient";

export class AlertMessages extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            alerts: [],
            shown: []
        }
    }

    componentDidMount() {
        if (this.props.alertsRefresh === true) {
            this.refreshAlerts();
            this.props.setAlertsRefresh(false);
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.alertsRefresh === true) {
            this.refreshAlerts();
            this.props.setAlertsRefresh(false);
        }
    }

    getAlerts(): Promise {
        return AlertsApiClient.checkForRecentAlerts(getUserId()).then(
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
            let filtered = alerts.filter(alert =>
                !this.state.shown.includes(alert) && !this.state.alerts.includes(alert));
            this.setState({
                alerts: filtered
            })
        }
    }

    refreshAlerts() {
        this.getAlerts().then(
            alerts => {
                this.updateAlerts(alerts)
            }
        ).catch(reason => {
            console.log('Alerts server error', reason);
        });
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
                {this.state.alerts.map(alertMessage =>
                    <AlertMessage message={alertMessage} onClick={() => this.closeAlert(alertMessage)}/>
                )}</div>
        );
    }
}