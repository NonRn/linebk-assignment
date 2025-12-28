import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import '../assets/styles/th-bank.css';
import Dialog from '../components/Dialog';
import alertUtils from '../utils/AlertUtils';

const QRScan = () => {
    const navigate = useNavigate();
    const [dialogData, setDialogData] = useState(alertUtils.close());

    useEffect(() => {
        setDialogData(
            alertUtils.alert({
                title: 'ระบบแจ้งเตือน',
                message: 'อนุญาตให้แอปเข้าถึงกล้องเพื่อสแกน QR code หรือ barcode',
                onClose: () => {
                    navigate(-1);
                },
            }),
        );
    }, []);

    const handleDialogClose = (callback) => {
        setDialogData(alertUtils.close());
        if (callback) {
            callback();
        }
    };

    return (
        <div className="wrap" style={{ minHeight: '100vh' }}>
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

export default QRScan; 
