import './Checkbox.css'
import EmotionsColour from "./EmotionsColour";

const Checkbox = ({ text, onChange }) => {

    return (
        <label className="checkbox-label">
            <input
                className="checkbox-input"
                type="checkbox"
                name="emotion"
                value={text.toUpperCase()}
                onChange={onChange}
            />
            <span className="custom-checkbox" style={{backgroundColor: EmotionsColour(text.toUpperCase())}} />
            {text}
        </label>
    );
};

export default Checkbox;
