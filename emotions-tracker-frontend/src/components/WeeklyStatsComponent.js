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

class WeeklyStatsComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            weeklyStats: WeeklyStats,
            serverError: false
        }
    }

    componentDidMount() {
        this.refreshStats();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.statsRefresh === true) {
            this.refreshStats();
            this.props.setStatsRefresh(false);
        }
    }

    getStats(): Promise {
        return StatsApiClient.weeklyStats(getUserId()).then(
            res => {
                if (res.ok) {
                    return res.json();
                } else {
                    return Promise.reject("Error on fetching last logs");
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
            console.log('Entry logging server error', reason);
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

export default WeeklyStatsComponent;

