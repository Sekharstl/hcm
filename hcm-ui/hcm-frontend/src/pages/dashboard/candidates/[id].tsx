import Layout from '../../../components/common/Layout';
import CandidateStepperForm from '../../../components/candidate/CandidateStepperForm';
import CandidateDetails from '../../../components/candidate/CandidateDetails';
import { useRouter } from 'next/router';

export default function CandidatePage() {
  const router = useRouter();
  const { query } = router;
  const isEdit = query.edit === '1';
  const { id } = query;

  return (
    <Layout>
      {isEdit ? (
        id ? <CandidateStepperForm candidateId={id as string} /> : null
      ) : (
        <CandidateDetails />
      )}
    </Layout>
  );
} 