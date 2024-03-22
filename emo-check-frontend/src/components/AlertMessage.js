import React from "react";
import ApiClient from "../services/ApiClient";
import './AlertMessage.css'

export class AlertMessage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            alertsAvailable: false,
            alerts: [],
            displayAlert: false,
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
        return ApiClient.checkForRecentAlerts(window.localStorage.getItem("userId")).then(
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
                if (!this.state.shown.includes(alert)) {
                    this.setState({
                        alerts: alerts,
                        // reset the flag
                        alertsAvailable: true
                    });
                }
            })
        }
    }

    refreshAlerts() {
        this.getAlerts().then(
            alerts => {
                if (alerts !== null) {
                    this.updateAlerts(alerts)
                    this.state.displayAlert = true;
                }
            }
        ).catch(reason => {
            this.setState({alertsAvailable: false});
            console.log('Alerts server error', reason);
        });
    }

    closeAlert() {
        this.state.alerts.map(alert => {
            this.state.shown.push(alert);
        });
        this.setState({
            alertsAvailable: false,
            displayAlert: false,
            alerts: []
        });
    }


        render()
        {
            return (
                <div>
                    {this.state.displayAlert && this.state.alerts.map(alertMessage =>
                    !this.state.shown.includes(alertMessage) &&
                        <div className="alert-div">
                            <span className="alert-close-btn" onClick={() => this.closeAlert()}>&times;</span>
                            <strong>Danger!</strong> {alertMessage}
                        </div>
                    )
                    }</div>
            );
        }
    }