package com.mejesticpay.rtpgateway.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("CustomDuplicateCheck")
public class CustomDuplicateCheckRepository implements DuplicateCheckRepository
{

    @Qualifier("DuplicateCheck") // inject Default Spring implementation here
    @Autowired
    private DuplicateCheckRepository duplicateCheckRepository;

    @Override
    public DuplicateCheck save(DuplicateCheck s)
    {
        try {
            return duplicateCheckRepository.save(s);
        }
        catch(DataIntegrityViolationException divException)
        {
            divException.printStackTrace();
            throw new DuplicateKeyException(divException.getMessage(), divException);
        }
        catch (Exception e) {
            System.out.println(e.getClass());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <S extends DuplicateCheck> Iterable<S> saveAll(Iterable<S> iterable) {
        return duplicateCheckRepository.saveAll(iterable);
    }

    @Override
    public Optional<DuplicateCheck> findById(Long aLong) {
        return duplicateCheckRepository.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return duplicateCheckRepository.existsById(aLong);
    }

    @Override
    public Iterable<DuplicateCheck> findAll() {
        return duplicateCheckRepository.findAll();
    }

    @Override
    public Iterable<DuplicateCheck> findAllById(Iterable<Long> iterable) {
        return duplicateCheckRepository.findAllById(iterable);
    }

    @Override
    public long count() {
        return duplicateCheckRepository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        duplicateCheckRepository.deleteById(aLong);
    }

    @Override
    public void delete(DuplicateCheck duplicateCheck) {
        duplicateCheckRepository.delete(duplicateCheck);
    }

    @Override
    public void deleteAll(Iterable<? extends DuplicateCheck> iterable) {
        duplicateCheckRepository.deleteAll(iterable);
    }

    @Override
    public void deleteAll() {
        duplicateCheckRepository.deleteAll();
    }
}
