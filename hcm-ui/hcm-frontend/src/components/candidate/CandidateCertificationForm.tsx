import React, { useState } from 'react';
import { Paper, Box, Grid, TextField, Button, IconButton, Snackbar } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import MuiAlert from '@mui/material/Alert';

// Placeholder for API call
async function saveCandidateCertifications(certifications: any[], candidateId: string) {
  // TODO: Implement actual API call
  return Promise.resolve();
}

interface CandidateCertificationFormProps {
  candidateId: string;
  onNext: () => void;
  onBack: () => void;
  certifications: any[];
  setCertifications: (certifications: any[]) => void;
}

const CandidateCertificationForm: React.FC<CandidateCertificationFormProps> = ({ candidateId, onNext, onBack, certifications, setCertifications }) => {
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleCertificationChange = (idx: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setCertifications(certifications.map((cert, i) => i === idx ? { ...cert, [name]: value } : cert));
  };

  const addCertification = () => {
    setCertifications([
      ...certifications,
      {
        certificationId: undefined,
        certificateName: '',
        issuedBy: '',
        issueDate: '',
        expiryDate: '',
        certificationName: '',
        issuingOrganization: '',
      },
    ]);
  };

  const removeCertification = (idx: number) => {
    setCertifications(certifications.filter((_, i) => i !== idx));
  };

  const handleSaveAndFinish = async () => {
    setLoading(true);
    setError(null);
    try {
      await saveCandidateCertifications(certifications, candidateId);
      setSuccess('Certifications saved successfully!');
      onNext();
    } catch (err) {
      setError('Failed to save certifications');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ mt: 3 }}>
      {certifications.map((cert, idx) => (
        <Paper key={idx} sx={{ p: 3, pt: 5, pr: 5, mb: 2, borderRadius: 3, position: 'relative' }}>
          <IconButton aria-label="delete" onClick={() => removeCertification(idx)} sx={{ position: 'absolute', top: 12, right: 12, zIndex: 1 }} disabled={certifications.length === 1}>
            <DeleteIcon />
          </IconButton>
          <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
            <TextField fullWidth label="Certification Name" name="certificationName" value={cert.certificationName} onChange={e => handleCertificationChange(idx, e)} />
            <TextField fullWidth label="Certificate Name" name="certificateName" value={cert.certificateName} onChange={e => handleCertificationChange(idx, e)} />
            <TextField fullWidth label="Issued By" name="issuedBy" value={cert.issuedBy} onChange={e => handleCertificationChange(idx, e)} />
            <TextField fullWidth label="Issuing Organization" name="issuingOrganization" value={cert.issuingOrganization} onChange={e => handleCertificationChange(idx, e)} />
            <TextField fullWidth label="Issue Date" name="issueDate" type="date" InputLabelProps={{ shrink: true }} value={cert.issueDate} onChange={e => handleCertificationChange(idx, e)} />
            <TextField fullWidth label="Expiry Date" name="expiryDate" type="date" InputLabelProps={{ shrink: true }} value={cert.expiryDate} onChange={e => handleCertificationChange(idx, e)} />
          </Box>
          {idx === certifications.length - 1 && (
            <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
              <Button variant="outlined" onClick={onBack}>Back</Button>
              <Button variant="outlined" onClick={addCertification}>Add Certification</Button>
              <Button variant="contained" color="primary" onClick={handleSaveAndFinish} disabled={loading}>Save & Finish</Button>
            </Box>
          )}
        </Paper>
      ))}
      <Snackbar open={!!success} autoHideDuration={1200} onClose={() => setSuccess(null)} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <MuiAlert onClose={() => setSuccess(null)} severity="success" sx={{ width: '100%' }} elevation={6} variant="filled">{success}</MuiAlert>
      </Snackbar>
      <Snackbar open={!!error} autoHideDuration={2000} onClose={() => setError(null)} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <MuiAlert onClose={() => setError(null)} severity="error" sx={{ width: '100%' }} elevation={6} variant="filled">{error}</MuiAlert>
      </Snackbar>
    </Box>
  );
};

export default CandidateCertificationForm; 