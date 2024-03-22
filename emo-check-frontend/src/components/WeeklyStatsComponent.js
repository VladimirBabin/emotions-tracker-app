import * as React from "react";
import ApiClient from "../services/ApiClient";
import './WeeklyStatsComponent.css'
import StatsBar from "../helper_components/StatsBar";

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

    // componentDidUpdate(prevProps, prevState, snapshot) {
    //     this.refreshStats();
    // }

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
                        <StatsBar backColor="mediumslateblue" stateName="Awful" percent={this.state.weeklyStats.awfulState}/>
                        <StatsBar backColor="cornflowerblue" stateName="Bad" percent={this.state.weeklyStats.badState}/>
                        <StatsBar backColor="yellowgreen" stateName="Ok" percent={this.state.weeklyStats.okState}/>
                        <StatsBar backColor="mediumseagreen" stateName="Good" percent={this.state.weeklyStats.goodState}/>
                        <StatsBar backColor="seagreen" stateName="Excellent" percent={this.state.weeklyStats.excellentState}/>
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