import './StatsBar.css'

const StatsBar = ({backColor, stateName, percent}) => {
    return (
        <div className="stats-container-div">
            <div className="stats-div-bar" style={{
                backgroundColor: backColor,
                width: percent * 10
            }}></div>
            <p className="stats-p-item">
                {stateName}: {percent}%
            </p>
        </div>

    )
};

export default StatsBar;