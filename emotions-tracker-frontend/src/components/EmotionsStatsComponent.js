import React from "react";
import StatsApiClient from "../services/StatsApiClient";
import {getUserId} from "../services/AuthApiClient";
import EmotionView from "../helper_components/EmotionView";
import "./EmotionsStateComponent.css"

export class EmotionsStatsComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            topEmotions: [],
            serverError: false
        }
    }

    componentDidMount() {
        this.refreshEmotions();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.statsRefresh === true) {
            this.refreshEmotions();
            this.props.setStatsRefresh(false);
        }
    }

    getEmotions(): Promise {
        return StatsApiClient.getTopEmotions(getUserId()).then(
            res => {
                if (res.ok) {
                    return res.json();
                } else {
                    return Promise.reject("Error on fetching top emotions stats");
                }
            }
        );
    }

    updateEmotions(emotions) {
        this.setState({
            topEmotions: emotions,
            serverError: false
        });
    }

    refreshEmotions() {
        this.getEmotions().then(stats => {
                this.updateEmotions(stats);
            }
        ).catch(reason => {
            this.setState({serverError: true});
            this.setState({topEmotions: null});
            console.log('Stats server error', reason);
        });
    }

    render() {
        if (this.state.serverError) {
            return (
                <div>We're sorry, but we can't display last week most logged emotions at the moment</div>
            );
        }
        return (
            <>
                <h5>Last week most logged emotions:</h5>
                <div className="emotions-state-div">
                    {this.state.topEmotions.map(emotion => <EmotionView key={emotion} emotion={emotion}/>)}
                </div>
            </>
        )
    }
}