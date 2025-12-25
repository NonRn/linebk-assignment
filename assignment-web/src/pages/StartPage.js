import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { connect } from 'react-redux';

import '../assets/styles/th-bank.css';
import Pin from './Pin';
import Splash from '../components/Splash';
import { getUserByIdApi } from '../services/UserService';
import Dialog from '../components/Dialog';
import alertUtils from '../utils/AlertUtils';


const StartPage = (props) => {
    const navigate = useNavigate();
    const { user, fetchUserData } = props;
    const [dialogData, setDialogData] = useState(alertUtils.close());

    useEffect(() => {
        const url = new URL(window.location.href);
        const userId = url.searchParams.get('userid');
        let timeoutId;

        if(userId) {
            timeoutId = setTimeout(async () => {
                const result = await fetchUserData(userId);
                if (result && !result.success && result.message) {
                    setDialogData(
                        alertUtils.alert({
                            title: 'ระบบแจ้งเตือน',
                            message: result.message,
                            onClose: () => {
                                navigate('/login');
                            },
                        }),
                    );
                }
            }, 2000); //delay for show splash screen lol
        } else {
            timeoutId = setTimeout(() => {
                navigate('/login');
            }, 2000);
        }
        return () => {
            if (timeoutId) {
                clearTimeout(timeoutId);
            }
        };
    }, [fetchUserData, navigate]);

    const handleDialogClose = (callback) => {
        setDialogData(alertUtils.close());
        if (callback) {
            callback();
        }
    };

    return (
        <div>
            {user && user.userId ? <Pin /> : <Splash />}
            <Dialog
                isOpen={dialogData.isOpen}
                title={dialogData.title}
                message={dialogData.message}
                confirmLabel={dialogData.confirmLabel}
                buttons={dialogData.buttons}
                onClose={() => handleDialogClose(dialogData.onClose)}
            />
        </div>
    );
};

const mapStateToProps = (state) => {
    const { user } = state.user ;
    return {
        user,
    };
}

const mapDispatchToProps = (dispatch) => ({
    async fetchUserData(userId) {
        try {
            const { data } = await getUserByIdApi(userId);
            if (!data || Object.keys(data).length === 0) {
                return { success: false, message: 'ไม่พบข้อมูลผู้ใช้', };
            }

            console.log('Fetched user data:', data);
            dispatch({ type: 'USER_DATA', payload: { ...data } });
            return { success: true };
        } catch (error) {
            console.error('Failed to fetch user data:', error);
            return { success: false, message: 'พบข้อผิดพลาด' };
        }
    },
});

export default connect(mapStateToProps, mapDispatchToProps)(StartPage); 
