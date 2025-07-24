import VendorDetails from '../../../components/vendor/VendorDetails';
import VendorForm from '../../../components/vendor/VendorForm';
import Layout from '../../../components/common/Layout';
import { useRouter } from 'next/router';

export default function VendorPage() {
  const router = useRouter();
  const isEdit = router.query.edit === '1';
  return (
    <Layout>
      {isEdit ? <VendorForm /> : <VendorDetails />}
    </Layout>
  );
} 