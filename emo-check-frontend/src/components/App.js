import './App.css'
import Header from "./Header";
import AuthContent from "./AuthContent";
import AppContent from "./AppContent";

function App() {
    return (
        <div>
            <Header pageTitle="Emotions Tracker app"/>
            <div className="container-fluid">
                <div className="row">
                    <div className="col">
                        <AppContent/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default App;