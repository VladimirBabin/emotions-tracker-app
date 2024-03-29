import "./LogsDateTimeView.css"

const LogsDateTimeView = ({ value }) => {
    return (
        <div>
        <p className="date-view-item">{new Date(value).toDateString()}</p>
        <p className="date-view-item">{new Date(value).toLocaleTimeString().substring(0, 5)}</p>
        </div>
    )
};

export default LogsDateTimeView;