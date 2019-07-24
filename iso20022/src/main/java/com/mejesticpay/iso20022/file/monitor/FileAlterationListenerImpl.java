package com.mejesticpay.iso20022.file.monitor;

import com.mejesticpay.iso20022.pacs008.CreditTransferMessageParser;
import com.mejesticpay.paymentbase.Genesis;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileAlterationListenerImpl implements FileAlterationListener
{
    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver)
    {
        //System.out.println("FileAlterationListener started on: " + fileAlterationObserver.getDirectory().getAbsolutePath());
    }

    @Override
    public void onDirectoryCreate(File file)
    {
        String absolutePath = file.getAbsolutePath();
        System.out.println("onDirectoryCreate: " + absolutePath);
        String fullPath = FilenameUtils.getFullPath(absolutePath);
        String extension = FilenameUtils.getExtension(absolutePath);
        String baseName = FilenameUtils.getBaseName(absolutePath);
        System.out.println(absolutePath + " , " + fullPath + " , " + extension + " , " + baseName);
    }

    @Override
    public void onDirectoryChange(File file)
    {
        System.out.println("onDirectoryChange: " + file.getAbsoluteFile());
    }

    @Override
    public void onDirectoryDelete(File file)
    {
        System.out.println("onDirectoryDelete: " + file.getAbsoluteFile());
    }

    @Override
    public void onFileCreate(File file)
    {
        String absolutePath = file.getAbsolutePath();
        System.out.println("onFileCreate: " + absolutePath);
        System.out.println("File Access read/write/execute: " + file.canRead() + "/" + file.canWrite() + "/" + file.canExecute());

        String fullPath = FilenameUtils.getFullPath(absolutePath);
        String extension = FilenameUtils.getExtension(absolutePath);
        String baseName = FilenameUtils.getBaseName(absolutePath);
        System.out.println(absolutePath + " , " + fullPath + " , " + extension + " , " + baseName);

        String archiveFolderAsString = file.getParentFile().getParentFile().getAbsoluteFile() + File.separator + "archive";
        File archiveFolder = new File(archiveFolderAsString);
        System.out.println("Archive Folder: " + archiveFolder.getAbsoluteFile());

        try {
            String data = FileUtils.readFileToString(file, Charset.defaultCharset());
            System.out.println("File Content:" + data);

            Genesis genesis = new CreditTransferMessageParser().createGenesis(new FileReader(file));

            FileUtils.moveFileToDirectory(file, archiveFolder, true);
        } catch (FileExistsException e)
        {
            for (int i = 1; i < 20; i++) {
                File destFile = new File(archiveFolder, baseName + "_" + i + "." + extension );
                try {
                    FileUtils.moveFile(file, destFile);
                } catch (IOException ex) {
                }
            }
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileChange(File file)
    {
        System.out.println("onFileChange: " + file.getAbsoluteFile());
    }

    @Override
    public void onFileDelete(File file)
    {
        System.out.println("onFileDelete: " + file.getAbsoluteFile());
    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver)
    {
        //System.out.println("FileAlterationListener stopped on: " + fileAlterationObserver.getDirectory().getAbsolutePath());
    }
}


