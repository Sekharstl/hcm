import React, { useState } from 'react';
import { Paper, Box, Grid, TextField, Button, IconButton, Snackbar } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import MuiAlert from '@mui/material/Alert';

// Placeholder for API call
async function saveCandidateSkills(skills: any[], candidateId: string) {
  // TODO: Implement actual API call
  return Promise.resolve();
}

interface CandidateSkillFormProps {
  candidateId: string;
  onNext: () => void;
  onBack: () => void;
  skills: any[];
  setSkills: (skills: any[]) => void;
}

const CandidateSkillForm: React.FC<CandidateSkillFormProps> = ({ candidateId, onNext, onBack, skills, setSkills }) => {
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleSkillChange = (idx: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setSkills(skills.map((skill, i) => i === idx ? { ...skill, [name]: value } : skill));
  };

  const addSkill = () => {
    setSkills([
      ...skills,
      {
        skillId: undefined,
        proficiencyLevel: '',
        yearsOfExperience: '',
      },
    ]);
  };

  const removeSkill = (idx: number) => {
    setSkills(skills.filter((_, i) => i !== idx));
  };

  const handleSaveAndNext = async () => {
    setLoading(true);
    setError(null);
    try {
      await saveCandidateSkills(skills, candidateId);
      setSuccess('Skills saved successfully!');
      onNext();
    } catch (err) {
      setError('Failed to save skills');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ mt: 3 }}>
      {skills.map((skill, idx) => (
        <Paper key={idx} sx={{ p: 3, pt: 5, pr: 5, mb: 2, borderRadius: 3, position: 'relative' }}>
          <IconButton aria-label="delete" onClick={() => removeSkill(idx)} sx={{ position: 'absolute', top: 12, right: 12, zIndex: 1 }} disabled={skills.length === 1}>
            <DeleteIcon />
          </IconButton>
          <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
            <TextField fullWidth label="Skill ID" name="skillId" value={skill.skillId || ''} onChange={e => handleSkillChange(idx, e)} />
            <TextField fullWidth label="Proficiency Level" name="proficiencyLevel" value={skill.proficiencyLevel} onChange={e => handleSkillChange(idx, e)} />
            <TextField fullWidth label="Years of Experience" name="yearsOfExperience" type="number" value={skill.yearsOfExperience} onChange={e => handleSkillChange(idx, e)} />
          </Box>
          {idx === skills.length - 1 && (
            <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
              <Button variant="outlined" onClick={onBack}>Back</Button>
              <Button variant="outlined" onClick={addSkill}>Add Skill</Button>
              <Button variant="contained" color="primary" onClick={handleSaveAndNext} disabled={loading}>Save & Next</Button>
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

export default CandidateSkillForm; 