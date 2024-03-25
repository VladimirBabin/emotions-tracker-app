import "./StateView.css"

const StateView = ({ value }) => {

    let colour;
    if (value === "AWFUL") {
        colour = "mediumslateblue";
    } else if (value === "BAD") {
        colour = "cornflowerblue";
    } else if (value === "OK") {
        colour = "yellowgreen";
    } else if (value === "GOOD") {
        colour = "mediumseagreen";
    } else {
        colour = "seagreen";
    }

    let state = value.charAt(0) + value.toLowerCase().slice(1);

    return (
        <p className="state-view-item" style={{backgroundColor: colour}}>
            {state}
        </p>
    );
};

export default StateView;