import * as React from "react";

class WeeklyStatsComponent extends React.Component {

    render() {
        return (
            <>
            <h5>Last week statistics:</h5>
            <div>
                    <ul>
                        <li>Bad: {this.props.weeklyStats.badState}%</li>
                        <li>Good: {this.props.weeklyStats.goodState}%</li>
                        <li>Excellent: {this.props.weeklyStats.excellentState}%</li>
                    </ul>
            </div>
            </>
        )
    }
}

export default WeeklyStatsComponent;

export const WeeklyStats = (badState, goodState, excellentState) => {
    return { badState: badState, goodState: goodState, excellentState: excellentState } }