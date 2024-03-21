import * as React from "react";
import './StatsBar.css'

export default class StatsBar extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            backColor: props.backColor,
            percent: props.percent
        }
    }

    render() {
        return (
            <p className="stats-item" style={{
                backgroundColor: this.backColor,
                width: this.percent}}>
                Awful: {this.percent}
            </p>
        )
    }
}