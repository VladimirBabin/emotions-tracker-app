import './Checkbox.css'

const Checkbox = ({ text, onChange, value, styleColor }) => {
    return (
        <label className="checkbox-label">
            <input
                className="checkbox-input"
                type="checkbox"
                name="emotion"
                value={value}
                onChange={onChange}
            />
            <span className="custom-checkbox" style={{backgroundColor: styleColor}} />
            {text}
        </label>
    );
};

export default Checkbox;
