import "./FormDateTimeView.css"

const FormDateTimeView = ({value}) => {
    return (
        <div className="row row-no-gutters">
            <div className="col-md-6">
                <p className="form-date-view-item">{new Date(value).toDateString()}</p>
            </div>
            <div className="col-md-6">
                <p className="form-date-view-item">{new Date(value).toLocaleTimeString().substring(0, 5)}</p>
            </div>
        </div>
    )
};

export default FormDateTimeView;