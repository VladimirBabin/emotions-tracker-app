import './StatsBar.css'
import StateColour from "./StateColour";

const StatsBar = ({stateName, percent}) => {
    return (
        <div className="stats-container-div">
            <div className="stats-div-bar" style={{
                backgroundColor: StateColour(stateName.toUpperCase()),
                width: (isNaN(percent) ? 0 : percent)  * 10
            }}></div>
            <p className="stats-p-item">
                {stateName}: {percent}%
            </p>
        </div>
    )
};

export default StatsBar;