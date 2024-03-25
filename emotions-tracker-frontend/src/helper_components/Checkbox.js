import './Checkbox.css'
import EmotionsColour from "./EmotionsColour";

const Checkbox = ({ text, onChange, value }) => {

    return (
        <label className="checkbox-label">
            <input
                className="checkbox-input"
                type="checkbox"
                name="emotion"
                value={value}
                onChange={onChange}
            />
            <span className="custom-checkbox" style={{backgroundColor: EmotionsColour(value)}} />
            {text}
        </label>
    );
};

export default Checkbox;
