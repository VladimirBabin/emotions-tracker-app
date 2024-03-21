import * as React from "react";
import ApiClient from "../services/ApiClient";
import './WeeklyStatsComponent.css'

class WeeklyStatsComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            weeklyStats: WeeklyStats,
        }
    }

    componentDidMount() {
        this.refreshStats();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        this.refreshStats();
    }

    refreshStats() {
        ApiClient.weeklyStats(window.localStorage.getItem("login")).then(res => {
            if (res.ok) {
                res.json().then(stats => {
                    this.setState({
                        weeklyStats: stats
                    })
                });
            }
        });
    }


    render() {
        return (
            <>
            <h5>Last week statistics:</h5>
            <div>
                    <ul>
                        <p className="stats-item" style={{backgroundColor: "mediumslateblue",
                            width: this.state.weeklyStats.awfulState}}>
                            Awful: {this.state.weeklyStats.awfulState}</p>
                        <p className="stats-item" style={{backgroundColor: "cornflowerblue",
                            width: this.state.weeklyStats.badState}}>
                            Bad: {this.state.weeklyStats.badState}</p>
                        <p className="stats-item" style={{backgroundColor: "yellowgreen",
                            width: this.state.weeklyStats.okState}}>
                            Ok: {this.state.weeklyStats.okState}</p>
                        <p className="stats-item" style={{backgroundColor: "mediumseagreen",
                            width: this.state.weeklyStats.goodState}}>
                            Good: {this.state.weeklyStats.goodState}</p>
                        <p className="stats-item" style={{backgroundColor: "seagreen",
                            width: this.state.weeklyStats.excellentState}}>
                            Excellent: {this.state.weeklyStats.excellentState}</p>
                    </ul>
            </div>
            </>
        )
    }
}

export default WeeklyStatsComponent;

export const WeeklyStats = (awfulState, badState, goodState, okState, excellentState) => {
    return {
        awfulState: awfulState,
        badState: badState,
        okState: okState,
        goodState: goodState,
        excellentState: excellentState
    }
}