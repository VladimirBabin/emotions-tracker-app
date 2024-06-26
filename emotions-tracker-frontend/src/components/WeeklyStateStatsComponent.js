import * as React from "react";
import StatsBar from "../helper_components/StatsBar";
import {getUserId} from "../services/AuthApiClient";
import StatsApiClient from "../services/StatsApiClient";

const WeeklyStats = (awfulState, badState, goodState, okState, excellentState) => {
    return {
        awfulState: awfulState,
        badState: badState,
        okState: okState,
        goodState: goodState,
        excellentState: excellentState
    }
}

class WeeklyStateStatsComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            weeklyStats: WeeklyStats,
            serverError: false
        }
    }

    componentDidMount() {
        setTimeout(this.refreshStats.bind(this), 50);
        setTimeout(this.refreshStats.bind(this), 150);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.statsRefresh === true) {
            setTimeout(this.refreshStats.bind(this), 50);
            setTimeout(this.refreshStats.bind(this), 150);
            this.props.setStatsRefresh(false);
        }
    }

    getStats(): Promise {
        return StatsApiClient.getWeeklyStateStats(getUserId()).then(
            res => {
                if (res.ok) {
                    return res.json();
                } else {
                    return Promise.reject("Error on fetching weekly state stats");
                }
            }
        );
    }

    updateStats(stats) {
        this.setState({
            weeklyStats: stats,
            serverError: false
        });
    }

    refreshStats() {
        this.getStats().then(stats => {
                this.updateStats(stats);
            }
        ).catch(reason => {
            this.setState({serverError: true});
            console.log('Stats server error', reason);
        });
    }


    render() {
        if (this.state.serverError) {
            return (
                <div>We're sorry, but we can't display last week statistics at the moment</div>
            );
        }
        return (
            <>
                <h5>Last week statistics:</h5>
                <div>
                    <ul>
                        <StatsBar stateName="Awful"
                                  percent={this.state.weeklyStats.awfulState}/>
                        <StatsBar stateName="Bad" percent={this.state.weeklyStats.badState}/>
                        <StatsBar stateName="Ok" percent={this.state.weeklyStats.okState}/>
                        <StatsBar stateName="Good"
                                  percent={this.state.weeklyStats.goodState}/>
                        <StatsBar stateName="Excellent"
                                  percent={this.state.weeklyStats.excellentState}/>
                    </ul>
                </div>
            </>
        )
    }
}

export default WeeklyStateStatsComponent;

