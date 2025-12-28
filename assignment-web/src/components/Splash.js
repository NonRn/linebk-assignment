import '../assets/styles/th-bank.css';

const Splash = (props) => {
    const { isBlur } = props;

    return (
        <div className="wrap">
        <div className="splash" style={isBlur ? {position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0, 0, 0, 0.5)'} : {}}>
            <div className="loader" />
        </div>
        </div>
    );
};

export default Splash;
