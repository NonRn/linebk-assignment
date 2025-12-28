import { useNavigate } from 'react-router-dom';

import '../assets/styles/th-bank.css';

const AddMoney = () => {
    const navigate = useNavigate();

    return (
        <div className="wrap" style={{ minHeight: '100vh' }}>
            <div style={{ textAlign: 'center', padding: '20px', marginTop: '10px' }}>
                <h2>แสกน QR Code <br/> เพื่อโอนเงินเข้าบัญชี** </h2>
                <img 
                    src={require('../assets/add_money_qrcode.jpg')} 
                    alt="QR Code สำหรับเติมเงิน" 
                    style={{ padding: '20px', maxWidth: '90%', maxHeight: '450px' }}
                />

                <p style={{ marginTop: '20px' }}>**บัญชีส่วนตัวผู้ทำ Assignment : โปรดตรวจสอบข้อมูลก่อนโอนเงิน</p>
                
                <button 
                    onClick={() => navigate(-1)} 
                    style={{ 
                        marginTop: '30px', 
                        padding: '12px 40px', 
                        fontSize: '16px',
                        backgroundColor: '#24c875',
                        color: '#FFFFFF',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer'
                    }}
                >
                    Back
                </button>
            </div>
        </div>
    );
};

export default AddMoney; 
