import * as React from "react";

class WeeklyStatsComponent extends React.Component {

    render() {
        return (
            <>
            <h5>Last week statistics:</h5>
            <div>
                    <ul>
                        <li>Bad: {this.props.weeklyStats.awfulState}%</li>
                        <li>Bad: {this.props.weeklyStats.badState}%</li>
                        <li>Bad: {this.props.weeklyStats.okState}%</li>
                        <li>Good: {this.props.weeklyStats.goodState}%</li>
                        <li>Excellent: {this.props.weeklyStats.excellentState}%</li>
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